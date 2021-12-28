package com.lolzdev.fabriccomputers.computer.luaj;

import com.lolzdev.fabriccomputers.computer.Computer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.DebugLib;

public class ComputerDebugLib extends DebugLib {

    private Computer computer;

    public ComputerDebugLib(Computer computer) {
        super();
        this.computer = computer;
    }

    @Override
    public void onInstruction(int pc, Varargs v, int top) {
        super.onInstruction(pc, v, top);

        if (this.computer == null || this.computer.isInterrupted()) {
            throw new RuntimeException("Terminated");
        }
    }
}
