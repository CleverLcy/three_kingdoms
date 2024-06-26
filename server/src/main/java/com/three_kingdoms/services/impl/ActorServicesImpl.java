package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.ActorDao;
import com.three_kingdoms.dao.AddrDao;
import com.three_kingdoms.dao.EventDao;
import com.three_kingdoms.domain.Actor;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.exception.BusinessException;
import com.three_kingdoms.services.ActorServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ActorServicesImpl implements ActorServices {
    @Autowired
    private ActorDao actorDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private AddrDao addrDao;

    @Override
    public IPage findAllToPage(Long current, Long size) {
        LambdaQueryWrapper<Actor> lqw = new LambdaQueryWrapper();
        lqw.select(Actor::getAid, Actor::getAFname, Actor::getATname,
                Actor::getBTime, Actor::getDTime, Actor::getAddrId, Actor::getPower);
        IPage page = new Page(current, size);
        return actorDao.selectPage(page, lqw);
    }

    public List<Actor> findAllActor() {
        List<Actor> actorList = actorDao.selectList(null);
        for (Actor actor : actorList) {
            actor.setAddr(findActorAddr(actor.getAid()));
            actor.setEventList(findEvents(actor.getAid()));
        }
        return actorList;
    }

    @Override
    public IPage findAllByName(String name, Page page) {
        LambdaQueryWrapper<Actor> lqw = new LambdaQueryWrapper<Actor>();
        lqw.select(Actor::getAid, Actor::getAFname, Actor::getATname,
                Actor::getBTime, Actor::getDTime, Actor::getAddrId, Actor::getPower);
        lqw.like(Actor::getAFname, name).or().like(Actor::getATname, name);
        IPage iPage = new Page(page.getCurrent(), page.getSize());
        return actorDao.selectPage(page, lqw);
    }

    @Override
    public Actor findById(Long aid) {
        return actorDao.selectById(aid);
    }

    @Override
    public List<Event> findEvents(Long aid) {
        try {
            String eventStr = actorDao.selectById(aid).getEvents();
            if (eventStr == null || eventStr.length() < 1) {
                return new ArrayList<>();
            }
            String[] events = eventStr.split(",");
            List<Event> eventList =
                    eventDao.selectBatchIds(Arrays.stream(events).toList());
            for (Event event : eventList) {
                event.setEContent("(考虑性能，此接口不显示事件内容)");
            }
            return eventList;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SELECT_ERR, "参数不合规");
        }
    }

    @Override
    public Addr findActorAddr(Long aid) {
        try {
            Actor actor = actorDao.selectById(aid);
            return addrDao.selectById(actor.getAddrId());
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SELECT_ERR, "参数不合规");
        }
    }

    @Override
    public IPage findAllByPower(IPage p, String name, String[] powers) {
        LambdaQueryWrapper<Actor> lqw = new LambdaQueryWrapper();
        lqw.select(Actor::getAid, Actor::getAFname, Actor::getATname,
                Actor::getBTime, Actor::getDTime, Actor::getAddrId, Actor::getPower);
        lqw.in(Actor::getPower, powers);
        if (name != null) {
            lqw.like(Actor::getAFname, name);
        }
        return actorDao.selectPage(p, lqw);
    }

    @Override
    public Integer save(Actor actor) {
        return actorDao.insert(actor);
    }

    @Override
    public Integer delete(Long aid) {
        return actorDao.deleteById(aid);
    }

    public Integer deleteMore(List<Long> aids) {
        int i = actorDao.deleteBatchIds(aids);
        return i;
    }

    @Override
    public Integer update(Actor actor) {
        return actorDao.updateById(actor);
    }
}
