jar -cvf ROOT.war *
cp ROOT.war ~/Software/multi_tomcat/tomcat/webapps2/

scp -r ROOT.war liuyong@192.168.31.100:/home/liuyong/Software/tomcat/webapps2/

