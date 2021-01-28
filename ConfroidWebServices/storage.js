var multer  = require('multer');
const fs = require("fs");

// Set The Storage Engine
const storage = multer.diskStorage({
    destination: function(req, file, cb) {
      fs.mkdirSync('./ressourcess/config-uploads/' +req.id_person, { recursive: true })
      cb(null, './ressourcess/config-uploads/' +req.id_person);
    },
    filename: function(req, file, cb){
      cb(null, file.originalname);
    }
});

var upload = multer({
    storage: storage,
    limits:{fileSize: 1000000}
})

exports.upload =  upload;
