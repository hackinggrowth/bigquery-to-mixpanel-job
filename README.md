# BigQuery To Mixpanel Job PoC

```sh
gradle run --runner=DataflowRunner \
 -Pdataflow-runner \
 --args="--project={projectName} --tempLocation={gcsLocation} --outputTable={targetTable}"
```