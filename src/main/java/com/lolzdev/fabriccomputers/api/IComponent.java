package com.lolzdev.fabriccomputers.api;

import org.luaj.vm2.LuaValue;

public interface IComponent {
    String getComponentType();
    String getComponentUUID();
    LuaValue getComponent();
}
