import http from 'k6/http';
import {sleep} from 'k6';
import { URLSearchParams } from 'https://jslib.k6.io/url/1.0.0/index.js';

export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(95)', 'p(99)', 'p(99.99)', 'count'],
    stages: [
        { duration: '15s', target: 80 }, // traffic ramp-up from 1 to 80 users over 15 seconds
        { duration: '90s', target: 80 }, // stay at 80 users for 90 seconds
        { duration: '15s', target: 0 }, // ramp-down to 0 users
    ],
};

export default function () {
    const url = new URL(`http://${__ENV.REMOTE_HOSTNAME}`);

    const res = http.get(url.toString());
    sleep(1);
}