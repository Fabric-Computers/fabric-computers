local io = os.loadLibrary("io")
local VERSION = "1.0"

_G.currentDirectory = ""

io.print("Fabric OS Shell v" .. VERSION)

if io.screen then
    local size = io.screen:getScreenSize()
    print(size[1])
end

while true do
    _G.currentColumn = 0
    io.write("> ")

    local cmd = io.readLine()
    if cmd:sub(1, 2) == "./" then
        if io.exists(_G.currentDirectory .. "/" .. cmd:sub(3, #cmd)) then
            os.runScript(_G.currentDirectory .. "/" .. cmd:sub(3, #cmd))
        end
    elseif io.exists("bin/" .. cmd:lower() .. ".lua") then
        os.run(cmd:lower())
        if cmd:lower() == "clear" then
            io.setCursor(0, 0)
        end
    else
        io.setForeground(0xFF0000)
        io.print("command not found")
        io.setForeground(0xFFFFFF)
    end
end