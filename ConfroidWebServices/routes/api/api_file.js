var express = require('express');
var router = express.Router();
var AuthHelper = require('./utils_auth');
var upload_storage = require('../../storage');

const fs = require("fs");

const outputPath = global.__basedir + "/resources/config-uploads/";


router.get('/files', AuthHelper.checkToken, async function(req, res) {
    var path_files = outputPath + req.id_person+'/';

    fs.readdir(path_files, function (err, files) {
      if (err) {
        res.status(500).send({
          message: "Unable to read files!",
        });
        throw err;
      }
  
      let fileInfos = [];
  
      files.forEach((file) => {
        fileInfos.push({
          name: file,
          url: global.baseUrl+ 'api/file/' + file,
        });
      });
  
      res.status(200).send(fileInfos);
    });
});

router.get('/file/:name', AuthHelper.checkToken, async function(req, res) {
    const fileName = '/' + req.params.name;
  
    res.download(outputPath + req.id_person + fileName, fileName, (err) => {
      if (err) {
        res.status(500).send({
          message: "Could not download the file. " + err,
        });
      }
    });
});

router.post('/upload', AuthHelper.checkToken, upload_storage.upload.single('file'), function(req, res) {
    console.log(req.file)
    
    if (req.file == undefined) {
        res.status(400).send({ message: "Please upload a file!" });
    } else {
        res.status(200).send({ message : "file recevied"})
    }
    
});




module.exports = router;