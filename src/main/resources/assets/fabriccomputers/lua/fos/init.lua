local fileSystem = fs

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
end

local init = fileSystem:readFile("fos.lua")
local func, err = load(init)
if func then
    local ok, i = pcall(func)
    if not ok then
        print("Failed to run: ", i)
    end
else
    print("Failed to run: ", err)
end