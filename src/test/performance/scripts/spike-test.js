import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 100 },  // 1. Warm up: Steady state at 100 users
        { duration: '10s', target: 100 },  //    Hold steady
        { duration: '5s', target: 2000 }, // 2. SPIKE! Jump to 2000 users in 5 seconds
        { duration: '30s', target: 2000 }, //    Hold the spike
        { duration: '5s', target: 100 },  // 3. Recovery: Drop back to 100 users
        { duration: '20s', target: 100 },  //    Verify recovery
        { duration: '5s', target: 0 },    //    Ramp down
    ],
    thresholds: {
        http_req_failed: ['rate<0.05'],    // Allow slightly higher failure rate during spike (5%)
        http_req_duration: ['p(95)<1000'], // Latency might spike, but should stay under 1s
    },
};

export default function () {
    const url = 'http://localhost:8080/employees';
    const payload = JSON.stringify({
        type: 'FULL_TIME',
        name: 'Spike Tester',
        email: 'spike@test.com',
        annualSalary: 100000,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'is status 201': (r) => r.status === 201,
    });

    sleep(1);
}

export function handleSummary(data) {
    return {
        '../results/spike-test-summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}

function htmlReport(data) {
    return `
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8" />
      <title>k6 Spike Test Summary</title>
      <style>
        body { font-family: sans-serif; margin: 2rem; }
        h1 { color: #333; }
        .metric { margin-bottom: 1rem; }
        .label { font-weight: bold; display: inline-block; width: 200px; }
        .value { color: #007bff; }
        .fail { color: red; }
      </style>
    </head>
    <body>
      <h1>k6 Spike Test Summary</h1>
      <div class="metric"><span class="label">Total Requests:</span> <span class="value">${data.metrics.http_reqs.values.count}</span></div>
      <div class="metric"><span class="label">Failed Requests:</span> <span class="value ${data.metrics.http_req_failed.values.rate > 0.05 ? 'fail' : ''}">${(data.metrics.http_req_failed.values.rate * 100).toFixed(2)}%</span></div>
      <div class="metric"><span class="label">Avg Response Time:</span> <span class="value">${data.metrics.http_req_duration.values.avg.toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">P95 Response Time:</span> <span class="value">${data.metrics.http_req_duration.values['p(95)'].toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">Max Response Time:</span> <span class="value">${data.metrics.http_req_duration.values.max.toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">Throughput:</span> <span class="value">${data.metrics.http_reqs.values.rate.toFixed(2)} req/s</span></div>
    </body>
    </html>
  `;
}

function textSummary(data, options) {
    return 'See spike-test-summary.html for full details.';
}
