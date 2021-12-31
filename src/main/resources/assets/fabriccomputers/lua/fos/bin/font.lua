local args = ...

local io = os.loadLibrary("io")

if (fileSystem:exists(args[1])) then

    local content = fileSystem:readFile(args[1])

    local font = {}
    local w=-1
    local h=-1
    local b=nil

    for line in content:gmatch("([^\n]*)\n?") do

        local tokens = {}

        for token in string.gmatch(line, "[^%s]+") do
            table.insert(tokens, token)
        end

        if #tokens == 0 then
            goto continue
        end

        if tokens[1] == "#" then
            goto continue
        end

        if tokens[1] == "w" then
            w = tonumber(tokens[2])
            goto continue
        end

        if tokens[1] == "h" then
            h = tonumber(tokens[2])
            goto continue
        end

        if tokens[1] == "d" then
            b = {}

            for i=2, #tokens do
                table.insert(b, tonumber(tokens[i]))
            end

            goto continue
        end

        if tokens[1] == "c" then
            font[string.char(tonumber(tokens[2]))] = {w=w, h=h, b=b}
            goto continue
        end

        ::continue::

    end

    io.setFont(font)
    io.print("Font loaded!")

else
    io.print("File not found!")
end

