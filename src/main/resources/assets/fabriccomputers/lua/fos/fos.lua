fileSystem = fs

_G.os = {}

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
end

function os.loadLibrary(lib)
    local content = fileSystem:readFile("lib/"..lib..".lua")
    local func, err = load(content)
    if func then
        local ok, i = pcall(func)
        if not ok then
            print("Cannot load library: ", i)
            return nil
        else
            return i
        end
    else
        print("Cannot load library: ", err)
        return nil
    end
end

local io = os.loadLibrary("io")
local event = os.loadLibrary("event")

_G.print = io.print

while true do
    local name, key = event.pollEvents()
    if name == "interrupt" then
        break
    elseif name == "key_down" then
        print("Key Pressed2!")
    end
end
