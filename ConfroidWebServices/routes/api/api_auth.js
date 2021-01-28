
var AuthHelper = require('./utils_auth');

var express = require('express');
var router = express.Router();
const path = require('path');

const dbAccount = require("../../database/account");
const jwt = require("jsonwebtoken");



router.post('/login', async function(req, res) {
    var usernameGiven = req.body.username;                  // Get data from request body
    var passwordGiven = req.body.password;
    
    if (!usernameGiven || !passwordGiven) {
        res.json({success: false, data: 'Please enter email and password.'});
        return;
    } 

    try {
      var userInfo = await dbAccount.findByUsernamePassword(usernameGiven, passwordGiven);
      
      console.log('ici_1 :' + userInfo);
      userInfo = JSON.parse(userInfo);
      
      if (userInfo) {              // Check if credentials matched
        console.log(userInfo);
        const token = AuthHelper.generateToken(userInfo);
        res.status(200).json({"username":usernameGiven, "password":passwordGiven, "token":token});
        //res.send(`Hello, ${users[0].username}`);
        console.log(token);
        //res.header('Authorization', "Bearer "+ token).redirect('/api/auth/');
        //res.header('x-authorization', "Bearer "+ token).redirect('/api/auth/');
    } else {
        res.status(401).send({data : 'Wrong username or password'});
      }
    } catch (err) {                              // Handle errors
      console.error('Database error:', err);  
      res.status(500).send('Error');
    }
});

router.post("/register", async (req, res) => {
      //const hashedPassword = await bcrypt.hash(req.body.password, 10);
  
      var username = req.body.username;                  // Get data from request body
      var password = req.body.password;
      
      if (!username || !password) {
        res.json({success: false, data: 'Please enter email and password.'});
        return;
      } 

      try {
        var users = await dbAccount.findByUsername(username);
    
        if (users.length == 1) {                      // Check if credentials matched
            res.status(401).json({"data": "Username already taken" });
        } else {
            //const hashPassword = AuthHelper.hashPassword(req.body.password);

            const data = await dbAccount.insert(username, password);
            console.log(data);
            res.status(200).json({"username":username, "password":password}); 
        }
      } catch (err) {                              // Handle errors
        console.error('Database error:', err);     
        res.status(500).send('Error');
        //res.render("/login");
    }

    
});


module.exports = router;