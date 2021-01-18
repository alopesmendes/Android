const http = require('http');
const app = require('./app');


const PORT = process.env.PORT || 8080;

const server = http.createServer(app);

server.listen(PORT, error => {
	if (error) {
    	return console.error(error)
  	}
  	
    console.log(`Votre app est disponible sur localhost: ${PORT}`);
});