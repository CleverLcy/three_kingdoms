import CryptoJS from "crypto-js";

const key = CryptoJS.enc.Utf8.parse("AA9000601BE69541");  //十六位十六进制数作为密钥
const iv = CryptoJS.enc.Utf8.parse('BBA111712CF7A652');   //十六位十六进制数作为密钥偏移量
 
//解密方法
export function decryption(word) {
  let encryptedHexStr = CryptoJS.enc.Hex.parse(word);
  let srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr);
  let decrypt = CryptoJS.AES.decrypt(srcs, key, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
  let decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
  return decryptedStr.toString();
}
 
//加密方法
export function encryption(word) {
  let srcs = CryptoJS.enc.Utf8.parse(word);
  let encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
  return encrypted.ciphertext.toString().toUpperCase();
}