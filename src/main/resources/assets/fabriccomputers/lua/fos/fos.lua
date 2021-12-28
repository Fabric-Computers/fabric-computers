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

function os.run(bin)
    local content = fileSystem:readFile("bin/"..bin..".lua")
    local func, err = load(content)
    if func then
        local ok, i = pcall(func)
        if not ok then
            print("Cannot run program: ", i)
        end
    else
        print("Cannot run program: ", err)
    end
end

local io = os.loadLibrary("io")

--_G.print = io.print
_G.error = io.error

os.run("shell")