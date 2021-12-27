package com.lolzdev.fabriccomputers.items;

import com.lolzdev.fabriccomputers.computer.ResourceFileSystem;

public class FixedFloppyDiskItem extends FloppyDiskItem{

    public FixedFloppyDiskItem(String resource) {
        super();

        this.fileSystem = new ResourceFileSystem(resource);
    }
}
