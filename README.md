# xyzla-java-plugins

```
557  mvn versions:set -DnewVersion=1.1.4 -DallowSnopShot=true
558  mvn versions:commit
563  mvn clean deploy
568  mvn versions:set -DnewVersion=1.1.5 -DallowSnopShot=true
569  mvn versions:commit
577  mvn clean deploy
597  mvn clean package
755  mvn clean package
850  mvn clean package
851  mvn clean deploy -DskipTests=true -P release
852  mvn clean package -DskipTests=true -P release
853  mvn clean package -DskipTests=true -P release
859  mvn clean package -DskipTests=true -P release
860  mvn clean package -DskipTests
861  mvn clean deploy -DskipTests
862  mvn clean packae
863  mvn clean package
878  mvn clean install
879  mvn clean
883  mvn clean package
887  mvn clean package
888  mvn clean install
894  mvn clean install
900  mvn versions:set -DnewVersion=1.1.2 -DallowSnopShot=true
901  mvn versions:commit
902  mvn versions:set -DnewVersion=1.1.2 -DallowSnopShot=true
903  mvn versions:commit
909  mvn clean compile
910  mvn versions:set -DnewVersion=1.1.2 -DallowSnopShot=true
911  mvn versions:commit
922  mvn clean deploy -projects xyzla-common,xyzla-spring
923  mvn clean deploy -projects xyzla-common,xyzla-spring -P release
```


```
mvn clean deploy -projects xyzla-spring-autoconfig,xyzla-spring-starter -P releas
 
mvn clean deploy -projects xyzla-common -P release
```