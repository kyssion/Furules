package org.mintflow;

import org.mintflow.async.result.AsyncResult;
import org.mintflow.handle.MintFlowHandlerMapper;
import org.mintflow.param.ParamWrapper;
import org.mintflow.handle.HandlerDataMap;
import org.mintflow.handle.async.AsyncFnHandler;
import org.mintflow.handle.sync.SyncFnHandler;
import org.mintflow.handle.util.MintFlowHandlerMapperBuilder;
import org.mintflow.scheduler.async.AsyncScheduler;
import org.mintflow.scheduler.async.FnAsyncEngineScheduler;
import org.mintflow.builder.FnMapperBuilder;
import org.mintflow.exception.InitMintFlowException;
import org.mintflow.exception.UserMintFlowException;
import org.mintflow.scheduler.sync.SyncFnEngineSyncScheduler;
import org.mintflow.scheduler.sync.SyncScheduler;

import java.util.List;

public class MintFlow {

    public static class MintFlowBuilder{

        private final HandlerDataMap handlerDataMap;

        //Represents a collection of processors, used for logic consumption graphs and processor mapping
        private final MintFlowHandlerMapper handlerDataMapper;

        public MintFlowBuilder(MintFlowHandlerMapper mapper){
            this.handlerDataMapper = mapper;
            this.handlerDataMap= new HandlerDataMap();
        }

        public MintFlowBuilder addFnMapper(String fnFilePath){
            try {
                this.handlerDataMap.addAll(FnMapperBuilder.build(handlerDataMapper, fnFilePath));
            } catch (Exception e) {
                e.printStackTrace();
                throw new InitMintFlowException("初始化FnMapper失败....");
            }
            return this;
        }

        public MintFlow build(){
            MintFlow MintFlow= new MintFlow();
            MintFlow.handlerDataMap = this.handlerDataMap;
            MintFlow.handlerDataMapper = this.handlerDataMapper;
            return MintFlow;
        }
    }

    private HandlerDataMap handlerDataMap;


    //Represents a collection of processors, used for logic consumption graphs and processor mapping
    private MintFlowHandlerMapper handlerDataMapper;

    protected MintFlow() {
        super();
    }

    public ParamWrapper runSync(String namespace, String process, ParamWrapper paramWrapper, SyncScheduler syncScheduler)  {
        List<SyncFnHandler> processFnHandlerList = getHandlerSync(namespace,process);
        if(syncScheduler ==null){
            syncScheduler = new SyncFnEngineSyncScheduler();
        }
        return syncScheduler.run(paramWrapper, processFnHandlerList);
    }

    public void runAsync(String namespace, String process , ParamWrapper paramWrapper, AsyncResult asyncResult) {
        List<AsyncFnHandler> processFnHandlerList = getHandlerAsync(namespace,process);
        AsyncScheduler asyncScheduler = new FnAsyncEngineScheduler(processFnHandlerList);
        asyncScheduler.next(paramWrapper,asyncResult);
    }

    private List<SyncFnHandler> getHandlerSync(String namespace, String process){
        checkNamespaceInfo(namespace);
        HandlerDataMap.ProcessDataMap processDataMap = this.handlerDataMap.getHandlerNamespaceMap().get(namespace);
        List<SyncFnHandler> syncFnHandlerList = processDataMap.getSyncFnDataMap().get(process);
        if (syncFnHandlerList == null) {
            throw new UserMintFlowException("未发现指定的process流程信息....");
        }
        return syncFnHandlerList;
    }

    private List<AsyncFnHandler> getHandlerAsync(String namespace,String process){
        checkNamespaceInfo(namespace);
        HandlerDataMap.ProcessDataMap processDataMap = this.handlerDataMap.getHandlerNamespaceMap().get(namespace);
        List<AsyncFnHandler> asyncFnHandlerList = processDataMap.getAsyncFnDataMap().get(process);
        if (asyncFnHandlerList == null) {
            throw new UserMintFlowException("未发现指定的process流程信息....");
        }
        return asyncFnHandlerList;
    }

    private void checkNamespaceInfo(String namespace){
        if (this.handlerDataMap == null) {
            throw new UserMintFlowException("fnMapper没有初始化,请使用initFnMapper方法初始化....");
        }
        HandlerDataMap.ProcessDataMap namespaceItem = this.handlerDataMap.getHandlerNamespaceMap().get(namespace);
        if (namespaceItem == null) {
            throw new UserMintFlowException("未发现指定的namespace信息....");
        }
    }

    public static MintFlowBuilder newBuilder(MintFlowHandlerMapper handlerDataMap){
        return new MintFlowBuilder(handlerDataMap);
    }
}
