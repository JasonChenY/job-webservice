1. geneate ROOT CA
openssl req -newkey rsa:2048 -x509 -days 5480 -keyout CARoot2048.key -out CARoot2048.crt -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=SignCert/CN=RootCA -passout pass:root1234
# -req:   request to generate new certificate
# -x509  generate self-signed certificate
# create root CA' private key with algorithm rsa 2048 bits, use passwd root1234
# create root self signed certificate with subject ....


### this step is equals to following steps:
#openssl genrsa -des3 -out ./demoCA/private/cakey.pem 2048
#openssl req -new -days 365 -key ./demoCA/private/cakey.pem -out cqreq.pem
#openssl ca -selfsign -in careq.pem -out cacert.pem

# private key is protected by passwd, password can be removed via following cmd:
# openssl rsa -in CARoot2048.key -out CARoot2048.key

# to view the certificate
openssl x509 -text -noout -in CARoot2048.crt

# translate the self-signed root ca to DER format, and upload to android or ios, then device will trust all the certificates signed by this ROOT CA.
(http://www-01.ibm.com/support/knowledgecenter/SSZH4A_6.0.0/com.ibm.worklight.help.doc/admin/c_ssl_config.html)
openssl x509 -in CARoot2048.crt -outform der -out CARoot2048-der.crt

2. client to request for certificate ( this can be done with keytool as well )
2.1 create private key
2.1.1 first geneate private key, then encypt it
openssl genrsa -out tiaonr.key 2048    # private key in cleartext

#encrypt the private key with aes 256 algo
openssl rsa -aes256 -in tiaonr.key -out tiaonr-aes256.key -passout pass:tiaonr

2.1.2 OR: encrype it while generating the private key 
(Note: -passout should be placed before 2048)
openssl genrsa -aes256 -out tiaonr-aes256.key -passout pass:tiaonr 2048


2.2 create request for certificate
# generate certificate request with extensions in config file
openssl req -new -key tiaonr-aes256.key -out tiaonr-request.csr -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=WebService/CN=www.tiaonr.com -extensions v3_req -config ./openssl.cnf -passin pass:tiaonr

# if request an intermediate CA certificate, e.g can again sign other certificate request
#openssl req -new -key tiaonr-aes256.key -out tiaonr-inter-ca-request.csr -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=DEV/CN=www.tiaonr.com -extensions v3_ca -extensions v3_req -config ./openssl.cnf -passin pass:tiaonr

# to view the request
openssl req -text -noout -in tiaonr-request.csr


3. sign the request
#openssl ca -in tiaonr-request.csr -out tiaonr.crt -cert CARoot2048.crt -keyfile CARoot2048.key -days 1826 -policy policy_anything -config ./openssl.cnf -passin pass:root1234

openssl x509 -req -days 3650 -in ../jks/tiaonr.csr -CA CARoot2048.crt -CAkey CARoot2048.key -CAcreateserial -out  tiaonr.x509.crt -extfile ./openssl.cnf -passin pass:root1234
openssl x509 -text -noout -in tiaonr.x509.crt


4. generte certificate chain
# this is not accepted by tomcat ????
#openssl pkcs12 -export -in tiaonr.x509.crt -inkey tiaonr-aes256.key -out tiaonr-chain.p12 -name tomcat  -CAfile  CARoot2048.crt -caname root  -chain -passin pass:tiaonr -passout pass:tiaonr

#useful method to download caterficate from server, can be done in browser directly as well.
openssl s_client -showcerts -connect 198.11.181.69:443
