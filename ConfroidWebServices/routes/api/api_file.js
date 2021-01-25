var express = require('express');
var router = express.Router();

const fs = require("fs");

const outputPath = global.__basedir + "/resources/config-uploads/";


router.get('/files', async function(req, res) {
    fs.readdir(outputPath, function (err, files) {
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

router.get('/file/:name', async function(req, res) {
    const fileName = req.params.name;
  
    res.download(outputPath + fileName, fileName, (err) => {
      if (err) {
        res.status(500).send({
          message: "Could not download the file. " + err,
        });
      }
    });
});

router.post('file/upload', (req, res) => {
    console.log(req.files)
    
    if (req.file == undefined) {
        res.status(400).send({ message: "Please upload a file!" });
    }

    if(req.files) {
        console.log(req.files)
        var file = req.files.file
        var filename = file.username
        console.log(filename)

        file.mv('./uploads/', filename, function(err) {
            if(err) {
                res.send(err)
            } else {
                res.send("File Uploaded")
            }
        })
    }
});




module.exports = router;