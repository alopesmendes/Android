
const jwt = require("jsonwebtoken");


  /**
   * Gnerate Token
   * @returns {string} token
   */
  function generateToken(personInfo) {
    const token = jwt.sign({
      id_person: personInfo.id_person,
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
                // when token is valid
                req.id_person = decoded.id_person;
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