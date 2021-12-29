local args = ...
local io = os.loadLibrary("io")

if #args > 0 then
    _G.currentDirectory = _G.currentDirectory .. "/" .. args[1]
else
    io.setForeground(0xFF0000)
    io.print("usage: cd <path>")
    io.setForeground(0xFFFFFF)
end