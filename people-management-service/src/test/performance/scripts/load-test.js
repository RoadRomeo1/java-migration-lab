import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 100 }, // Ramp up to 100 users
        { duration: '30s', target: 1000 }, // Ramp up to 1000 users (Stress)
        { duration: '10s', target: 0 },   // Ramp down
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        http_req_failed: ['rate<0.01'],   // Error rate should be less than 1%
    },
};

export default function () {
    const url = 'http://localhost:8080/people';
    const payload = JSON.stringify({
        personType: 'EMPLOYEE_FULL_TIME',
        name: 'K6 Tester',
        email: 'k6@test.com',
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
        '../results/load-test-summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}

// Minimal HTML report generator for k6 (embedded to avoid external dependencies)
function htmlReport(data) {
    return `
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8" />
      <title>k6 Load Test Summary</title>
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
      <h1>k6 Load Test Summary</h1>
      <div class="metric"><span class="label">Total Requests:</span> <span class="value">${data.metrics.http_reqs.values.count}</span></div>
      <div class="metric"><span class="label">Failed Requests:</span> <span class="value ${data.metrics.http_req_failed.values.rate > 0 ? 'fail' : ''}">${(data.metrics.http_req_failed.values.rate * 100).toFixed(2)}%</span></div>
      <div class="metric"><span class="label">Avg Response Time:</span> <span class="value">${data.metrics.http_req_duration.values.avg.toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">P95 Response Time:</span> <span class="value">${data.metrics.http_req_duration.values['p(95)'].toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">Max Response Time:</span> <span class="value">${data.metrics.http_req_duration.values.max.toFixed(2)} ms</span></div>
      <div class="metric"><span class="label">Throughput:</span> <span class="value">${data.metrics.http_reqs.values.rate.toFixed(2)} req/s</span></div>
    </body>
    </html>
  `;
}

// Minimal text summary generator
function textSummary(data, options) {
    return 'See load-test-summary.html for full details.';
}
