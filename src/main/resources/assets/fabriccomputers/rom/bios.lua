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

_G.fileSystems = fs:getFilesystems()

for key, value in pairs(_G.fileSystems) do
    value:mount(value:getUUIDOrRandom())
    value:writeFile("isDisk", "yes")

    print(value:readFile("init.lua"))

    if value:exists("init.lua") then
        local init = value:readFile("init.lua")
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
end

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

