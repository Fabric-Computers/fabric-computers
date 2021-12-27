_G.bios = {}

fs:mount(computer:getId())

function _G.bios.getScreenSize()
    local size = computer:getScreenSize()
    return size[1], size[2]
end

_G.fileSystems = fs:getFilesystems()

for key, value in pairs(_G.fileSystems) do
    value:mount(value:getUUIDOrRandom())
    value:writeFile("isDisk", "yes")

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

