local fos = _G.fileSystems["storage0"]:readFile("fos.lua")

local init = _G.fileSystems["storage0"]:readFile("fos.lua")
local func, err = load(init)
if func then
    local ok, i = pcall(func)
    if not ok then
        print("Failed to run: ", i)
    end
else
    print("Failed to run: ", err)
end