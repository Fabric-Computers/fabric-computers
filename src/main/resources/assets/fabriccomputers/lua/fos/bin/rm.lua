local args = ...
local io = os.loadLibrary("io")

if #args > 0 then
    print(io.remove(_G.currentDirectory .. "/" .. args[1]))
else
    io.setForeground(0xFF0000)
    io.print("usage: rm <path>")
    io.setForeground(0xFFFFFF)
end