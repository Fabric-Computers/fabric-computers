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
local thread = os.loadLibrary("thread")

--_G.print = io.print
_G.error = io.error

local redstones = {}
local output = 1

io.print("Gaming pc")

for i=0, 5 do
    local k = computer:getComponent(i)
    if k then
        if k:getComponentType() == "redstone" then
            table.insert(redstones, k)
            io.print("Redstone component found")
        end
    end
end

thread.waitForAny(
        function() while true do io.print("Thread1") computer:sleep(5e+9) coroutine.yield() end end,
        function()
            while true do io.print("Thread") computer:sleep(5e+9) coroutine.yield()  end
        end)