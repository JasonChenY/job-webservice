topdir=/sdk/tmp/spring/solr-pagination/conf/cert
#Problems for keytool and openssl
#1. keytool dont support import key alonely, try to generate key with keytool and generate csr with that key; 
#keytool -genkey -alias tiaonr -keyalg RSA -validity 3650 -dname "CN=www.tiaonr.com, OU=tiaonr, O=ZhiSheng Inc, L=SH, ST=SH, C=CN" -keypass tiaonr -keystore tomcat.keystore -storepass tomcat
#keytool -certreq -keyalg RSA -alias tiaonr -ext san=ip:192.168.137.128,ip:198.11.181.69,dns:www.tiaonr.com -file tiaonr.csr -keystore tomcat.keystore -storepass tomcat -keypass tiaonr
#2. shit: agency can't sign this csr, always complain stateOrProvinceName mismatch, even in error display message, they are exactly same. fuck!!!!

openssl genrsa -des3 -out tiaonr.key 1024
#SAN specified in openssl.cnf
openssl req -new -key tiaonr.key -out tiaonr.csr -passin pass:tiaonr -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=tiaonr/CN=www.tiaonr.com -extensions v3_req -config openssl.cnf -days 3650

#send tiaonr.csr to agency ca for sign

#receive certificate from $topdir/agency/rootCA/newcerts/01.pem

#generate certifcate chain:
cp $topdir/agency/rootCA/cacert.pem chain.pem
cat $topdir/root/rootCA/cacert.pem >> chain.pem

#verify certifcate
openssl verify -CAfile chain.pem $topdir/agency/rootCA/newcerts/01.pem

#translate to pkcs12 format
openssl pkcs12 -export -in $topdir/agency/rootCA/newcerts/01.pem -inkey tiaonr.key -out tiaonr.p12 -chain -CAfile chain.pem -passin pass:tiaonr -passout pass:tiaonr

# due to problems mentioned in beginning, can only use importkeystore from p12 to jks store
keytool -importkeystore -deststorepass tomcat -destkeypass tiaonr -destkeystore tomcat.keystore -srckeystore tiaonr.p12 -srcstoretype PKCS12 -srcstorepass tiaonr
# change the key alias from 1 to tiaonr, !!!! tomcat complains cant find the keyAlias after rename, fuck!
#keytool -changealias -alias 1 -destalias tiaonr -keystore tomcat.keystore -storepass tomcat -keypass tiaonr

############### Tomcat ###############
#a. add tomcat.keystore to conf/server.xml
    <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true"  
               maxThreads="150" scheme="https" secure="true"
               sslProtocol="TLS"   
               keystoreFile="conf/tomcat.keystore"
               keystorePass="tomcat"
               keyAlias="1" 
               keyPass="tiaonr"/>

#b. tomcat/bin/setenv.sh (this is DEPRECATED, otherwise will cause problem when connecting to qq/weibo)
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
#JAVA_OPTS="$JAVA_OPTS -Djavax.net.ssl.trustStore=/sdk/tmp/ca/tiaonr/tomcat.keystore -Djavax.net.ssl.trustStorePassword=tomcat"

#c. import SELF-SIGNED ROOT certificate to client's JVM keystore
keytool -import -trustcacerts -alias tiaonrRootCA -file /sdk/tmp/spring/solr-pagination/conf/cert/root/rootCA/cacert.pem -keystore /etc/ssl/certs/java/cacerts -storepass changeit
(Note, not following keystore)
keytool -import -trustcacerts -alias tiaonrRootCA -file /sdk/tmp/spring/solr-pagination/conf/cert/root/rootCA/cacert.pem -keystore "/sdk/tools/jdk1.8.0_51/jre/lib/security/cacerts" -storepass changeit
#list jvm's keystore
keytool -list -keystore /sdk/tools/jdk1.8.0_51/jre/lib/security/cacerts -storepass changeit


############### Browser ###############
#add /sdk/tmp/ca/root/rootCA/cacert.pem to trusted root certification.
#(browser can add tiaonr.p12 to personal certification area as well, but will report exception if root cacert.pem not added)
#then when browser access the website will not report exception and establish secure connection directly.

############### Android ###############
#translate root ca to DER format, and upload to sdcard, device will trust all the certificates signed by this ROOT CA.
(http://www-01.ibm.com/support/knowledgecenter/SSZH4A_6.0.0/com.ibm.worklight.help.doc/admin/c_ssl_config.html)
openssl x509 -in /sdk/tmp/ca/root/rootCA/cacert.pem -outform der -out rootca-der.crt
adb push rootca-der.crt /storage/sdcard0/rootca-der.crt
Install the self signed root ca in "Settings -> Security -> Certificate -> User"

#cordova plugin VerifySslCertificate ( if used )
#should update the finger to corresponding info in certificate

## for android, to install self signed cert as system CA (ROOT needed).
openssl x509 -inform PEM -subject_hash_old -in /sdk/memo/search/certificate/jks/tiaonr.cer | head -1
cat /sdk/memo/search/certificate/jks/tiaonr.cer >  5ed36f99.0
openssl x509 -inform PEM -text -in /sdk/memo/search/certificate/jks/tiaonr.cer -out /dev/null >> 5ed36f99.0
adb push 5ed36f99.0 /storage/sdcard0/5ed36f99.0
adb shell
su
mount -o remount,rw /system
cp /sdcard/5ed36f99.0 /system/etc/security/cacerts/
chmod 644 /system/etc/security/cacerts/5ed36f99.0
ls -al -Z
mount -o remount,ro /system
reboot

Now can see the certification installed in "Settings -> Security -> Certificate -> System"
