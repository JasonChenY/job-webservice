=========================Enable SSL in tomcat====================
#### server side:
# only generate the keystore with pass tomcat, and one private key with pass tiaonr
keytool -genkey -alias tiaonr -keyalg RSA -validity 3650 -dname "CN=www.tiaonr.com, OU=WebService, O=Tiaonaer Technology Co. Ltd., L=SH, ST=SH, C=CN" -keypass tiaonr -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat

# to change certificate, e.g add more san
#keytool -selfcert -alias tiaonr san=ip:xxx,ip:yyyy,ip:zzzz,dns:mmm -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat
# to delete a certificate
#keytool -delete -alias xxx -keystore .... -storepass ....

# by default -storetype JKS
keytool -list -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat 

#### to generate certificate request
keytool -certreq -keyalg RSA -alias tiaonr -ext san=ip:192.168.137.128,ip:198.11.181.69,dns:www.tiaonr.com -file tiaonr.csr -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat -keypass tiaonr

#### refer to openssl for how to sign the request ###########


#### import the cerfificate, root CA, intermediate CA and server certification
keytool -import -trustcacerts -alias CARoot -file ../openssl/CARoot2048.crt -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat 
#keytool -import -trustcacerts -alias CAIntermediate -file CAIntermediate.crt -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat 
keytool -import -trustcacerts -alias CATiaonaer -file ../openssl/tiaonr.x509.crt -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat

keytool -list -v -keystore tomcat.keystore -storepass tiaonr

#export certificate for client side
keytool -export  -alias tiaonr -file /sdk/memo/search/certificate/jks/tiaonr.cer -keystore /sdk/memo/search/certificate/jks/tomcat.keystore -storepass tomcat


