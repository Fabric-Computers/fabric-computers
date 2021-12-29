local fileSystem = fs

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
end

local init = fileSystem:readFile("fos.lua")
local func, err = load(init, "fos.lua")
if func then
    local ok, i = pcall(func)
    if not ok then
        error("Failed to run: ", i)
    end
else
    error("Failed to run: ", err)
end