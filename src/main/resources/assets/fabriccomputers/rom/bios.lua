_G.bios = {}

fs:mount(computer:getId())


function _G.bios.getScreenSize()
    local size = computer:getScreenSize()
    return size[1], size[2]
end

local width, height = bios.getScreenSize()
for x=0, width-1 do
    for y=0, height-1 do
        computer:setPixel(x, y, 0x000000)
    end
end

for i=0, 5 do
    local k = computer:getFloppyFs(i)
    if k then
        k:mount(k:getUUIDOrRandom())
        k:writeFile("isDisk", "yes")

        if k:exists("init.lua") then
            local init = k:readFile("init.lua")
            local func, err = load(init)
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
    local init = fs:readFile("init.lua")
    local func, err = load(init)
    if func then
        local ok, i = pcall(func)
        if not ok then
            print("Failed to run: ", i)
        end
    else
        print("Failed to run: ", err)
    end
end

