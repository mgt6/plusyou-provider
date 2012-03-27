#+U Provider
This project exposes a REST api used by the +U application.

Some features of the api

* search for volunteering opportunities
* retrieve detailed information of volunteering opportunity
* notify charity via email when user registers for volunteering opportunity
* ...

##software installation
* java 6 (<http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html>)
* jasypt (<http://sourceforge.net/projects/jasypt/files>)
* maven 3 (<http://maven.apache.org/download.html>)
* tomcat 6 (<http://tomcat.apache.org/download-60.cgi>)
* mysql 5 (<http://dev.mysql.com/downloads/mysql>)

##jasypt encrypt example
encrypt input="This is my message to be encrypted" password=MYPAS_WORD

##google setup
* create google account (<https://accounts.google.com/SignUp?service=mail&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ltmpl=default>)

##maven setup
add or modify profiles (settings.xml)

    <profile>
        <id>plusyou-default</id>

        <properties>
            <database.provider.url>jdbc:mysql://localhost/XXX</database.provider.url><!--url of provider database-->
            <database.provider.username>XXX</database.provider.username><!--username of provider database-->
            <encryptor.password>XXX</encryptor.password><!--passphrase to encrypt passwords-->

            <smtp.from>donotreply@XXX</smtp.from>
            <smtp.host>smtp.gmail.com</smtp.host>
            <smtp.port>465</smtp.port>
            <smtp.username>XXX@gmail.com</smtp.username>
            <smtp.password>XXX</smtp.password><!--encrypted password-->

            <test.smtp.from>donotreply@XXX</test.smtp.from>
            <test.sftp.host>XXX</test.sftp.host>
            <test.sftp.port>22</test.sftp.port>
            <test.sftp.user>XXX</test.sftp.user>
            <test.sftp.password>XXX</test.sftp.password><!--encrypted password-->

            <test.smtp.to>XXX</test.smtp.to>
            <test.smtp.subject>test mail server</test.smtp.subject>
            <test.smtp.text.template>test_mail_server.vm</test.smtp.text.template>
        </properties>
    </profile>

    <profile>
        <id>plusyou-development</id>

        <activation>
            <property>
                <name>env</name>
                <value>dev</value>
            </property>
        </activation>

        <properties>
            <database.provider.password>XXX</database.provider.password><!--password of provider database-->

            <server.home>XXX</server.home><!--tomcat home folder-->
            <server.internet.accessible>false</server.internet.accessible><!--disable charity notification and daily csv import-->
        </properties>
    </profile>

    <profile>
        <id>plusyou-production</id>

        <activation>
            <property>
                <name>env</name>
                <value>prd</value>
            </property>
        </activation>

        <properties>
            <database.provider.password>XXX</database.provider.password><!--password of provider database-->

            <server.home>XXX</server.home><!--tomcat home folder-->
            <server.internet.accessible>true</server.internet.accessible><!--enable charity notification and daily csv import-->
        </properties>
    </profile>

activate plusyou-default profile (settings.xml)

    <activeProfiles>
        <activeProfile>plusyou-default</activeProfile>
    </activeProfiles>

##tomcat setup
add database connection settings (context.xml)

    <Resource
        type="javax.sql.DataSource"
        name="plusyou-provider-database"
        username="XXX"
        password="XXX"
        driverClassName="com.mysql.jdbc.Driver"
        url="jdbc:mysql://localhost/XXX"
        maxActive="35"
        maxIdle="2"
        maxWait="5000"
        removeAbandoned="true"
        removeAbandonedTimeout="60"
        testOnBorrow="true"
        validationQuery="SELECT 1"/>

##mysql setup
run maven commando

    mvn -Denv=dev clean process-test-resources

execute sql file target/test-classes/dbmaintain.sql as root user in mysql

run maven commando

    mvn -Denv=dev org.dbmaintain:dbmaintain-maven-plugin:2.4:updateDatabase

##populate database
development

    execute sql file src/main/config/database/development-data.sql as {database.provider.username} user in mysql
    change if needed the values of the latitude and longitude columns (opportunities table)

production

    execute sql file src/main/config/database/file-transfer-record.sql as {database.provider.username} user in mysql
    note: don't forget to change the placeholders

##useful maven commando's
create war file

    mvn -Denv=dev clean package

create war file and deploy to tomcat

    mvn -Denv=dev clean install

update database with sqls from src/main/config/database/scripts

    mvn -Denv=dev org.dbmaintain:dbmaintain-maven-plugin:2.4:updateDatabase