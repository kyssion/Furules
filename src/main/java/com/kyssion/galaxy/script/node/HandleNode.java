package com.kyssion.galaxy.script.node;

import com.kyssion.galaxy.script.type.HandleTypeEnum;

public class HandleNode extends Node {

    @Override
    public HandleTypeEnum getType() {
        return HandleTypeEnum.Handle;
    }
}