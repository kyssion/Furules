package org.melkweg.scheduler.async;

import org.melkweg.param.ParamWrapper;
import org.melkweg.async.result.AsyncResult;
import org.melkweg.exception.HandleUseException;
import org.melkweg.handle.FnHandler;
import org.melkweg.handle.async.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FnAsyncEngineScheduler implements AsyncScheduler {

    private Iterator<AsyncFnHandler> handlerIterable;
    private final List<AsyncFnHandler> handlerList;

    public FnAsyncEngineScheduler(List<AsyncFnHandler> handlerList){
        this.handlerIterable = handlerList.iterator();
        this.handlerList = handlerList;
    }

    @Override
    public void next(ParamWrapper paramWrapper, AsyncResult asyncResult) {
        if(!handlerIterable.hasNext()){
            asyncResult.doResult(paramWrapper);
            return;
        }
        FnHandler asyncFnHandle = handlerIterable.next();
        switch (asyncFnHandle.getType()){
            case SAMPLE_HANDLE_ASYNC:
                AsyncSampleFnHandler asyncSampleFnHandler = (AsyncSampleFnHandler) asyncFnHandle;
                asyncSampleFnHandler.asyncHandle(paramWrapper,asyncResult,this);
                break;
            case REORDER_HANDLE_ASYNC:
                //强制转化为 同步组建类 handle
                AsyncReorderFnHandler asyncReorderFnHandler = (AsyncReorderFnHandler) asyncFnHandle;
                asyncReorderFnHandler.asyncHandle(paramWrapper,asyncResult,this);
                break;
            case CONDITION_HANDLE_WRAPPER_ASYNC:
                //强制转化为 同步组建类 handle
                AsyncConditionFncHandlerWrapper asyncConditionFncHandlerWrapper = (AsyncConditionFncHandlerWrapper) asyncFnHandle;
                asyncConditionFncHandlerWrapper.asyncHandle(paramWrapper,asyncResult,this);
                break;
            case CYCLE_HANDLE_ASYNC:
                //强制转化为 同步组建类 handle
                AsyncCycleFnHandler asyncCycleFnHandler = (AsyncCycleFnHandler) asyncFnHandle;
                asyncCycleFnHandler.asyncHandle(paramWrapper,asyncResult,this);
                break;
            default:
                throw new HandleUseException("出现未知类型，不能在迭代器中运行，name："+asyncFnHandle.getName()+" type:"+asyncFnHandle.getType().getName());
        }
    }

    @Override
    public AsyncScheduler clone() throws CloneNotSupportedException {
        super.clone();
        return new FnAsyncEngineScheduler(new ArrayList<>(this.handlerList));
    }

    @Override
    public void reset() {
        this.handlerIterable = handlerList.iterator();
    }
}