package org.mintflow.handler.async.sample;

import org.mintflow.annotation.MintFlowHandler;
import org.mintflow.param.ParamWrapper;
import org.mintflow.async.result.AsyncResult;
import org.mintflow.scheduler.async.AsyncScheduler;
import org.mintflow.handle.async.AsyncSampleFnHandler;

@MintFlowHandler(name = "async_show_start_handle")
public class AsyncShowStartHandler extends AsyncSampleFnHandler {

    public AsyncShowStartHandler(String name) {
        super(name);
    }

    @Override
    public void asyncHandler(ParamWrapper params, AsyncResult asyncResult, AsyncScheduler asyncScheduler) {
        params.setContextParam("show_start",true);
        asyncScheduler.next(params,asyncResult);
    }
}
