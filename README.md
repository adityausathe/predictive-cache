# User-based Predictive Caching Framework
### :heavy_check_mark: Working

## Information
**Detailed information about the project can be found in ```documentation.pdf```.** Following is an attempt to briefly summarize key aspects of the idea and its implementation. 
## Functionality
- The project aims at creating a standalone framework to analyze users' application-usage behavior.
- It collects data about the resource-consumption by users, e.g. who queried which page at what time.
- Based on this information, the framework derives usage-trends which dictate a user's application access-behavior. The trend embodies temporal information about access-patterns.
- The trend is then consumed to predict next resource/page/api a user is likely invoke, and precompute/prepare the response associated with that resource ahead of time. If the user indeed invokes the predicted resource, then the precomputed response is served thus minimizing the turn around time.
## Implementation 
- The functionality is offered by two modules, backed by a NoSQL database. Pattern-mining and trend-derivation are done by the analytics module; Prediction and access data collection tasks are performed by the embedded framework.
- Embedded framework is a spring-based embedded framework-support, which sits inside the client application. It collects configuration and access-log data, it also provides mechanisms to predict and cache predicted resource ahead of time.
- Analytics module works independently and can be invoked as a batch process. It consumes access-log data and produces trends. 
- A framework-database is used as a medium of sharing the data between the two module.

## Dependencies
- Java environment for running client-app.
- Python 3 environment is used by analytics scripts.
- Mongo DB
