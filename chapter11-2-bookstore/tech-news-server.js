const news = [
    'Apress merged with Springer.',
    'VMWare buys Pivotal for a ridiculous amount of money!',
    'Twitter was hacked!',
    'Amazon launches reactive API for DynamoDB.',
    'Java 17 will be released in September 2021.',
    'The JavaScript world is still "The Wild Wild West".',
    'Java modules, still a topic that developers frown upon.'
];

const http = require('http');
const ws = require('ws');

const server = new ws.Server({noServer: true});

function accept(req, res) {
    if (!req.headers.upgrade || req.headers.upgrade.toLowerCase() !== 'websocket') {
        res.end();
        return;
    } else {
        console.log("Upgrading to using WebSocket!")
    }

    if (!req.headers.connection.match(/\bupgrade\b/i)) {
        res.end();
        return;
    }

    server.handleUpgrade(req, req.socket, Buffer.alloc(0), onConnect);
}

function onConnect(ws) {
    let interval = null;
    ws.on('message', function (message) {
        console.log("From client: " + message);
        let rate = 100; // ms
        if(message === 'Slow down mate!') {
            rate = 1000;
            clearInterval(interval); // stop emitting values until the next setInterval is called with the new rate value
        } else if (message === 'Faster mate!') {
            rate = 100;
            clearInterval(interval);  // stop emitting values until the next setInterval is called with the new rate value
        }

        interval = setInterval(() => {
            //examine the socket and send more data
            if (ws.bufferedAmount === 0) {
                ws.send(news[Math.floor(Math.random() * news.length)]);
                console.log('bufferedAmount after onmessage: ' + ws.bufferedAmount)
                console.log("the rate: " +  rate);
            }
        }, rate);

        // keep the connection up for 1 minute
        setTimeout(() => ws.close(1000, "Bye!"), 60000);
    });
}


if (!module.parent) {
    http.createServer(accept).listen(3000);
} else {
    exports.accept = accept;
}
