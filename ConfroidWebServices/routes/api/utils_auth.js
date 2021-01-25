
const jwt = require("jsonwebtoken");


  /**
   * Gnerate Token
   * @returns {string} token
   */
  function generateToken(personInfo) {
    const token = jwt.sign({
      userId: personInfo.userId,
      username: personInfo.username,
      password: personInfo.password
    },
      "SECREET", // process.env.SECRET
       { expiresIn: '30m' }
      );
    return token;
  }
  
function checkToken(req, res, next) {
  const token = req.headers['x-access-token'] || req.headers['authorization'];
  console.log(req.headers);

    if (token) {
        jwt.verify(token, "SECREET", (err, decoded) => {
            if (err) {
              res.status(401).json({ message: 'Invalid token provided' });
              return;
            } else {
                // token is valid
                req.userID = decoded.userID;
                next();
            }
        })
    } else {
      res.status(401).json({ message: 'No token provided' });
    }
  }
  



module.exports = {
  generateToken,
  checkToken,
  checkToken
};