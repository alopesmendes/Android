# Android
Confroid application will allow the user to change the configuration of certains applications. The user will be able to stock the changements locally or by login into his account.

## Installation

### Prerequisites
- Express, NodeJs
- Android Studio

### Linux 
```bash
$ sudo apt-get update
$ sudo apt-get install nodejs npm
```

Or download the package for the website [nodejs](https://nodejs.org/en/download/)

### Windows or Mac
Refer to the website [nodejs](https://nodejs.org/en/download/)


### Npm dependencies 
```bash
$ sudo npm install -g nodemon
$ npm install express --save
$ npm install sqlite3
$ npm install knex multer
$ npm install swagger-jsdoc swagger-ui-express
```


## Usage

### Linux
Firstly we have to launch the program NodeJs. **nodemon** is preferable since we can change the programn and there no need to restart the program. But we can also only use node
```bash
Android/ConfroidWebServices$ nodemon server.js
[nodemon] 2.0.6
[nodemon] to restart at any time, enter `rs`
[nodemon] watching path(s): *.*
[nodemon] watching extensions: js,mjs,json
[nodemon] starting `node index.js`
Votre app est disponible sur localhost:8080 !
```

```bash
$ lt -p 8080 --subdomain valkyroid
your url is: https://valkyroid.loca.lt
```

or 

```bash
Android/ConfroidWebServices$ node server.js
Votre app est disponible sur localhost:8080 !
```

```bash
$ lt -p 8080 --subdomain valkyroid
your url is: https://valkyroid.loca.lt
```

Then we launch AndroidStudio and run the application

### Windows 
```bash
Android/ConfroidWebServices$ nodemon server.js
[nodemon] 2.0.6
[nodemon] to restart at any time, enter `rs`
[nodemon] watching path(s): *.*
[nodemon] watching extensions: js,mjs,json
[nodemon] starting `node index.js`
Votre app est disponible sur localhost:8080 !
```

```bash
$ lt -p 8080 --subdomain valkyroid
your url is: https://valkyroid.loca.lt
```

or 

```bash
Android/ConfroidWebServices$ node server.js
Votre app est disponible sur localhost:8080 !
```

```bash
$ lt -p 8080 --subdomain valkyroid
your url is: https://valkyroid.loca.lt
```

Then we launch AndroidStudio and run the application

## Contributors
- Ailton LOPES MENDES
- Jonathan CHU
- Fabien LAMBERT--DELAVAQUERIE
- Akram MALEK
- Gérald LIN
