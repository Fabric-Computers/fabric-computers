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

_G.print = io.print

local redstones = {}
local output = 1

print("Gaming pc")

for i=0, 5 do
    local k = computer:getComponent(i)
    if k then
        if k:getComponentType() == "redstone" then
            table.insert(redstones, k)
            print("Redstone component found")
        end
    end
end



thread.create(function()
    while true do
        local name = event.pollEventsParallel()
        if name == "interrupt" then
            thread.done = true
            break
        end
        computer:sleep(2000)
        coroutine.yield()
    end
end)

thread.create(function()


    while true do
        for i, redstone in pairs(redstones) do
            if redstone then
                redstone:setOutput(output)
            end

        end
        output = output + 1
        if output > 15 then
            output = 0
        end
        coroutine.yield()
    end
end)

thread.waitForAll()