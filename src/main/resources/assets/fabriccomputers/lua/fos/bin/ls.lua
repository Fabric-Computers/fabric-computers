local io = os.loadLibrary("io")

local fileSystem = fs

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
end

for i, k in pairs(fileSystem:list("/")) do
    io.print(k .. " ")
end