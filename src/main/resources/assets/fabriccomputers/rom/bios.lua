_G.bios = {}

print("Loading BIOS")

local ARROW_DOWN = 264


fs:mount(computer:getId())

local screen

for i=0, 5 do
    local component = computer:getComponent(i)
    if component then
        if component:getComponentType() == "screen" then
            screen = component
        end
    end
end

function _G.bios.getScreenSize()
    if screen then
        local size = screen:getScreenSize()
        return size[1], size[2]
    end
    return 0, 0
end

local width, height = bios.getScreenSize()

for x=0, width-1 do
    for y=0, height-1 do
        if screen then
            screen:setPixel(x, y, 0x000000)
        end
    end
end

for i=0, 5 do
    local k = computer:getFloppyFs(i)
    if k then
        k:mount(k:getUUIDOrRandom())
        k:writeFile("isDisk", "yes")

        if k:exists("init.lua") then
            _G.fileSystem = k
            local init = k:readFile("init.lua")
            local func, err = load(init, "init.lua")
            _G.isDisk = true
            _G.diskIndex = i
            if func then
                local ok, i = pcall(func)
                if not ok then
                    print("Failed to run: ", i)
                end
            else
                print("Failed to run: ", err)
            end
        end
    end
end

_G.isDisk = false
_G.diskIndex = 0

if fs:exists("init.lua") then
    _G.fileSystem = fs
    local init = fs:readFile("init.lua")
    local func, err = load(init, "init.lua")
    if func then
        local ok, i = pcall(func)
        if not ok then
            for x=0, width-1 do
                for y=0, height-1 do
                    if screen then
                        screen:setPixel(x, y, 0x0000FF)
                    end
                end
            end
        end
    else
        for x=0, width-1 do
            for y=0, height-1 do
                if screen then
                    screen:setPixel(x, y, 0x0000FF)
                end
            end
        end
    end
end