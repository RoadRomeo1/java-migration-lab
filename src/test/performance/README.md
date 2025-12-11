# Performance Testing

This directory contains all performance testing resources for the Java 21 Employee Management application.

## Directory Structure

```
src/test/performance/
├── scripts/              # k6 test scripts
│   ├── load-test.js     # Stress test (ramps to 1000 users)
│   └── spike-test.js    # Spike test (sudden burst to 2000 users)
├── results/              # Test result HTML reports
│   ├── load-test-summary.html
│   └── spike-test-summary.html
├── java/                 # Java-based load testers
│   └── com/example/javamigrationlab/performance/
│       └── SimpleLoadTester.java
└── PERFORMANCE_TESTING.md  # Detailed test strategy and results

```

## Quick Start

### Prerequisites
- Application running on `http://localhost:8080`
- k6 installed (or use the Java-based tester)

### Running Tests

**k6 Tests:**
```bash
# From project root
cd src/test/performance/scripts

# Run stress test
k6 run load-test.js

# Run spike test
k6 run spike-test.js
```

**Java Load Tester:**
```bash
# From project root
java --enable-preview --source 21 "src/test/java/com/example/javamigrationlab/performance/SimpleLoadTester.java"
```

### Viewing Results
- HTML reports are automatically generated in `src/test/performance/results/`
- Open them in any browser for detailed metrics

## Test Configurations

### Current Setup
- **Latency Simulation**: 50ms (simulates network/DB delay)
- **Virtual Threads**: Enabled
- **Connection Pool**: 50
- **GC**: Generational ZGC

See `PERFORMANCE_TESTING.md` for full test history and analysis.
