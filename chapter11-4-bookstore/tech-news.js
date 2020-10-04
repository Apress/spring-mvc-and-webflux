const http = require('http');
const sys = require('util');
const fs = require('fs');

const hostname = 'localhost';
const port = 3000;

const news = [
    'Apress merged with Springer.',
    'VMWare buys Pivotal for a ridiculous amount of money!',
    'Twitter was hacked!',
    'Amazon launches reactive API for DynamoDB.',
    'Java 17 will be released in September 2021.',
    'The JavaScript world is still "The Wild Wild West".',
    'Java modules, still a topic that developers frown upon.'
];

const server = http.createServer((req, res) => {
    res.setHeader('Content-Type', 'text/event-stream;charset=UTF-8');
    res.setHeader('Cache-Control', 'no-cache');
    // only if you want anyone to access this endpoint
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.flushHeaders();

    // Sends a SSE every 2 seconds on a single connection.
    setInterval(function() {
        res.write('data:'+news[Math.floor(Math.random() * news.length)] + '\n\n');
    }, 2000);
});

server.listen(port, hostname, () => {
    console.log(`Event stream available at http://${hostname}:${port}/techNews`);
});

// test with  `curl http://localhost:3000/techNews -H "Accept:text/event-stream" -v`