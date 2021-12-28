local io = os.loadLibrary("io")
local VERSION = "1.0"

io.print("Fabric OS Shell v" .. VERSION)

if io.screen then
    local size = io.screen:getScreenSize()
    print(size[1])
end

while true do
    io.currentColumn = 0
    io.write("> ")

    local cmd = io.readLine()
    if io.exists("bin/" .. cmd:lower() .. ".lua") then
        os.run(cmd:lower())
    else
        io.setForeground(0xFF0000)
        io.print("command not found")
        io.setForeground(0xFFFFFF)
    end
end