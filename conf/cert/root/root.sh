mkdir -p rootCA
mkdir -p rootCA/{private,certs,crl,newcerts}
touch rootCA/private/.rand
touch rootCA/index.txt
touch rootCA/serial
echo "01" > rootCA/serial 

export RANDFILE="rootCA/private/.rnd"; 
openssl genrsa -des3 -out /sdk/tmp/ca/root/rootCA/private/cakey.pem 1024
openssl req -new -x509 -key /sdk/tmp/ca/root/rootCA/private/cakey.pem -out /sdk/tmp/ca/root/rootCA/cacert.pem -extensions v3_ca -passin pass:rootca -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=Cert/CN=RootCA

#openssl rsa -in /sdk/tmp/ca/root/rootCA/private/cakey.pem -text -noout
#openssl x509 -in /sdk/tmp/ca/root/rootCA/cacert.pem -text -noout

#following two steps can be done agency as well.
#openssl genrsa -des3 -out agency.key 1024
#openssl req -new -key agency.key -out agency.csr

openssl ca -in /sdk/tmp/ca/agency/agency.csr -extensions v3_ca -config ./openssl.cnf -passin pass:rootca

send /sdk/tmp/ca/root/rootCA/newcerts/01.pem to agency ( agency.key as well if we generated the key for agency )
