-- This file is required for installing FOS

local fstab = {}

fstab["fos.lua"] = {}
fstab["fos.lua"]["isDirectory"] = false

fstab["init.lua"] = {}
fstab["init.lua"]["isDirectory"] = false

fstab["lib"] = {}
fstab["lib"]["isDirectory"] = true
fstab["lib"]["content"] = {}

fstab["lib"]["content"]["io.lua"] = {}
fstab["lib"]["content"]["io.lua"]["isDirectory"] = false

fstab["lib"]["content"]["thread.lua"] = {}
fstab["lib"]["content"]["thread.lua"]["isDirectory"] = false

fstab["lib"]["content"]["event.lua"] = {}
fstab["lib"]["content"]["event.lua"]["isDirectory"] = false

fstab["lib"]["content"]["keys.lua"] = {}
fstab["lib"]["content"]["keys.lua"]["isDirectory"] = false

fstab["bin"] = {}
fstab["bin"]["isDirectory"] = true
fstab["bin"]["content"] = {}

fstab["bin"]["content"]["clear.lua"] = {}
fstab["bin"]["content"]["clear.lua"]["isDirectory"] = false

fstab["bin"]["content"]["font.lua"] = {}
fstab["bin"]["content"]["font.lua"]["isDirectory"] = false

fstab["bin"]["content"]["ls.lua"] = {}
fstab["bin"]["content"]["ls.lua"]["isDirectory"] = false

fstab["bin"]["content"]["cd.lua"] = {}
fstab["bin"]["content"]["cd.lua"]["isDirectory"] = false

fstab["bin"]["content"]["mkdir.lua"] = {}
fstab["bin"]["content"]["mkdir.lua"]["isDirectory"] = false

fstab["bin"]["content"]["shell.lua"] = {}
fstab["bin"]["content"]["shell.lua"]["isDirectory"] = false

fstab["bin"]["content"]["rm.lua"] = {}
fstab["bin"]["content"]["rm.lua"]["isDirectory"] = false

fstab["bin"]["content"]["touch.lua"] = {}
fstab["bin"]["content"]["touch.lua"]["isDirectory"] = false

fstab["bin"]["content"]["nano.lua"] = {}
fstab["bin"]["content"]["nano.lua"]["isDirectory"] = false

fstab["usr"] = {}
fstab["usr"]["isDirectory"] = true
fstab["usr"]["content"] = {}

fstab["usr"]["content"]["font.sbf"] = {}
fstab["usr"]["content"]["font.sbf"]["isDirectory"] = false

fstab["usr"]["content"]["font8x8.sbf"] = {}
fstab["usr"]["content"]["font8x8.sbf"]["isDirectory"] = false

fstab["usr"]["content"]["init.sh"] = {}
fstab["usr"]["content"]["init.sh"]["isDirectory"] = false

return fstab