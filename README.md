# RUN PROJECT

1. Download plate_recognition Folder.
2. Get deps, get data
3. Run training
4. Validate


**Get deps**
```
lein deps
```

**Get data**
```
lein run -m license-plate.getdata
```

**Training**
```
lein run -m licence-plate.training
```

**Validation**
```
lein run -m license-plate.validate
```


------------------------------------------------


# clojure-installation
Using CNN in clojure for classificate license plate

**INSTALLING CLOJURE** [Leiningen Page](https://leiningen.org/)

For use clojure on windows, Leinning will be used.

1. Download [lein.bat](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein.bat) and save in some PATH
2. Add this PATH in the Environment Variables

![Environment Variable](/images/variableEntorno.png)


**TEST CLOJURE**
```
lein repl
```
![HelloWorld](/images/clojureHelloWorld.png)


**CREATE NEW PROJECT**
```clojure
lein new app [Project_NAME]
```

**RUN PROJECT**
1. Download deps
```clojure
lein deps
```
2. Run project
```clojure
lein run
```



---------------------
**UBUNTU**
JAVA
```
yum install java-1.7.0-openjdk
```

CLOJURE
```
curl -O https://download.clojure.org/install/linux-install-1.9.0.381.sh
chmod +x linux-install-1.9.0.381.sh
sudo ./linux-install-1.9.0.381.sh
```
