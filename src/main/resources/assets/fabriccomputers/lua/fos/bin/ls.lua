local args = ...

local io = os.loadLibrary("io")

local fileSystem = fs

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
end

local path = _G.currentDirectory

if #args > 0 then
    path = _G.currentDirectory .. args[1]
end

if (fileSystem:exists(path)) then
    for i, k in pairs(fileSystem:list(path)) do
        if fileSystem:isDir(k) then
            io.setForeground(0x0000FF)
        elseif k:sub(#k-3, #k) == ".lua" then
            io.setForeground(0xFF0000)
        end
        io.print(k)
        io.setForeground(0xFFFFFF)
    end
else
    io.setForeground(0xFF0000)
    io.print("file not found")
    io.setForeground(0xFFFFFF)
end