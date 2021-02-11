
var AuthHelper = require('./utils_auth');

var express = require('express');
var router = express.Router();
const path = require('path');

const dbAccount = require("../../database/account");
const jwt = require("jsonwebtoken");
// Nodejs encryption with CTR
const crypto = require('crypto');
const algorithm = 'aes-256-cbc';
const key = crypto.randomBytes(32);
const iv = crypto.randomBytes(16);

function encrypt(text) {
 let cipher = crypto.createCipheriv('aes-256-cbc', Buffer.from(key), iv);
 let encrypted = cipher.update(text);
 encrypted = Buffer.concat([encrypted, cipher.final()]);
 return { iv: iv.toString('hex'), encryptedData: encrypted.toString('hex') };
}

function decrypt(text) {
 let iv = Buffer.from(text.iv, 'hex');
 let encryptedText = Buffer.from(text.encryptedData, 'hex');
 let decipher = crypto.createDecipheriv('aes-256-cbc', Buffer.from(key), iv);
 let decrypted = decipher.update(encryptedText);
 decrypted = Buffer.concat([decrypted, decipher.final()]);
 return decrypted.toString();
}


router.post('/login', async function(req, res) {
    var usernameGiven = req.body.username;                  // Get data from request body
    var passwordGiven = req.body.password;
    
    if (!usernameGiven || !passwordGiven) {
        res.json({success: false, data: 'Please enter username and password.'});
        return;
    }

    console.log(`passwordGiven:${passwordGiven}`)
    var encrtyptedPassword = encrypt(passwordGiven);
    console.log(`login enc:${encrtyptedPassword}`)
    try {
      var userInfo = await dbAccount.findByUsernamePassword(usernameGiven, encrtyptedPassword.encryptedData);
      console.log('ici_1 :' + userInfo);
      userInfo = JSON.parse(userInfo);
      var decryptedPassword = decrypt(encrtyptedPassword)
      console.log(`decrypted:${decryptedPassword}`)
      
      if (userInfo) {              // Check if credentials matched
        console.log(userInfo);
        const token = AuthHelper.generateToken(userInfo);
        res.status(200).json({"username":usernameGiven, "password":decryptedPassword, "token":token});
        console.log(token);
    } else {
        res.status(401).send({data : 'Wrong username or password'});
      }
    } catch (err) {                              // Handle errors
      console.error('Database error:', err);  
      res.status(500).send({data : 'Database Error'});
    }
});

router.post("/register", async (req, res) => {
      //const hashedPassword = await bcrypt.hash(req.body.password, 10);
  
      var username = req.body.username;                  // Get data from request body
      var password = req.body.password;
      
      if (!username || !password) {
        res.json({success: false, data: 'Please enter username and password.'});
        return;
      } 

      try {
        var users = await dbAccount.findByUsername(username);
    
        if (users.length == 1) {                      // Check if credentials matched
            res.status(401).json({"data": "Username already taken" });
        } else {
            //const hashPassword = AuthHelper.hashPassword(req.body.password);
            var encrtyptedPassword = encrypt(password)
            const data = await dbAccount.insert(username, encrtyptedPassword.encryptedData);
            console.log(data);
            console.log(`encrypted:${encrtyptedPassword}`)
            res.status(200).json({"username":username, "password":encrtyptedPassword.encryptedData}); 
        }
      } catch (err) {                              // Handle errors
        console.error('Database error:', err);     
        res.status(500).send({"data": "Database error" });
    }

    
});


module.exports = router;