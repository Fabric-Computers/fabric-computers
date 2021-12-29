fileSystem = fs

_G.os = {}

if _G.isDisk then
    fileSystem = computer:getFloppyFs(_G.diskIndex)
    local fstab = load(fileSystem:readFile("fstab.lua"))()
    for k, v in pairs(fstab) do
        if not v["isDirectory"] then
            fs:writeFile(k, fileSystem:readFile(k))
        else
            fs:makeDir(k)
            for k1, v1 in pairs(fstab[k]["content"]) do
                if not v1["isDirectory"] then
                    fs:writeFile(k .. "/" .. k1, fileSystem:readFile(k .. "/" .. k1))
                end
            end
        end
    end

    return
end

function os.loadLibrary(lib)
    local content = fileSystem:readFile("lib/"..lib..".lua")
    local func, err = load(content, "lib/"..lib..".lua")
    if func then
        local ok, i = pcall(func)
        if not ok then
            error(i)
            return nil
        else
            return i
        end
    else
        error(err)
        return nil
    end
end

function os.run(bin, ...)
    local content = fileSystem:readFile("bin/"..bin..".lua")
    local func, err = load(content, "bin/" .. bin .. ".lua")
    if func then
        local ok, i = pcall(func, ...)
        if not ok then
            error(i)
        end
    else
        error(err)
    end
end

function os.runScript(bin, ...)
    local content = fileSystem:readFile(bin)
    local func, err = load(content, "bin")
    if func then
        local ok, i
        if args then
            ok, i = pcall(func, ...)
        else
            ok, i = pcall(func)
        end
        if not ok then
            error(i)
        end
    else
        error(err)
    end
end

local io = os.loadLibrary("io")

--_G.print = io.print
_G.error = io.error

os.run("shell")