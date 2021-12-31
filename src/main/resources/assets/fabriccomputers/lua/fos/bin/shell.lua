local io = os.loadLibrary("io")
local VERSION = "1.0"

_G.currentDirectory = "/"

if io.screen then
    local size = io.screen:getScreenSize()
end

function split(s, delimiter)
    result = {};
    for match in (s..delimiter):gmatch("(.-)"..delimiter) do
        table.insert(result, match);
    end
    return result;
end

function copyTable(t1, t2)
    for i, k in pairs(t1) do
        table.insert(t2, i, k)
    end
end

-- for now just load the font
-- in the future execute commands from the init script instead
-- the font command prints the "Loaded font!" here
os.run("font", {"/usr/font.sbf"})
io.print("Fabric OS Shell v" .. VERSION)

while true do
    _G.currentColumn = 0
    io.write("> ")

    local cmd = io.readLine()

    cmd = split(cmd, " ")
    local args = {}
    copyTable(cmd, args)
    table.remove(args, 1)

    cmd = cmd[1]

    if cmd:sub(1, 2) == "./" then
        if io.exists(_G.currentDirectory .. "/" .. cmd:sub(3, #cmd)) then
            os.runScript(_G.currentDirectory .. "/" .. cmd:sub(3, #cmd), table.unpack(args))
        else
            io.setForeground(0xFF0000)
            io.print("file not found")
            io.setForeground(0xFFFFFF)
        end
    elseif io.exists("bin/" .. cmd:lower() .. ".lua") then
        os.run(cmd:lower(), args)
        if cmd:lower() == "clear" then
            io.setCursor(0, 0)
        end
    else
        io.setForeground(0xFF0000)
        io.print("command not found")
        io.setForeground(0xFFFFFF)
    end
end