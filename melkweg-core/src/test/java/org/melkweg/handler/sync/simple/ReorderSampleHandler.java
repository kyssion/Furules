package org.melkweg.handler.sync.simple;

import org.melkweg.annotation.MelkwegHandler;
import org.melkweg.test.syncBaseTest.ReorderTest;
import org.melkweg.handle.sync.SyncSampleFnHandler;
import org.melkweg.param.ParamWrapper;

@MelkwegHandler(name = "reorder_sample_handle")
public class ReorderSampleHandler extends SyncSampleFnHandler {

    public ReorderSampleHandler(String name) {
        super(name);
    }

    @Override
    public ParamWrapper handle(ParamWrapper params) {
        String itme = params.getParam(String.class);
        params.setParam(itme+ ReorderTest.ADD_DATA);
        return params;
    }
}